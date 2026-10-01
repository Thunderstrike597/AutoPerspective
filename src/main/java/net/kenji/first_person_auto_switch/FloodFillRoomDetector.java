package net.kenji.first_person_auto_switch;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public final class FloodFillRoomDetector {

    /** enclosed = fill terminated on its own; volume = number of air-ish blocks reached. */
    public record Result(boolean enclosed, int volume) {
        public static final Result OPEN = new Result(false, 0);
    }

    private static final Direction[] DIRS = Direction.values();


    // Reused between scans to avoid per-scan allocations
    private final LongOpenHashSet visited = new LongOpenHashSet(1024);
    private final LongArrayList queue = new LongArrayList(1024);
    private final BlockPos.MutableBlockPos cur = new BlockPos.MutableBlockPos();
    private final BlockPos.MutableBlockPos next = new BlockPos.MutableBlockPos();

    private boolean hasCache = false;
    private long cachedOrigin;
    private int ticksSinceScan;
    private Result cached = Result.OPEN;

    public FloodFillRoomDetector() {

    }

    /** Call once per client tick. Rescans only if the origin block changed or the cache is stale. */
    public Result get(Level level, BlockPos origin) {
        long key = origin.asLong();
       // max Chebyshev distance from origin, e.g. 20
        int recheckTicks = ConfigClient.CHECK_TICKS.get();  // periodic rescan so doors/broken blocks are noticed

        if (!hasCache || key != cachedOrigin || ++ticksSinceScan >= recheckTicks) {
            cached = scan(level, origin);
            cachedOrigin = key;
            ticksSinceScan = 0;
            hasCache = true;
        }
        return cached;
    }

    /** Force a rescan on the next get(), e.g. from a block-update hook near the player. */
    public void invalidate() {
        hasCache = false;
    }

    private Result scan(Level level, BlockPos origin) {
        visited.clear();
        queue.clear();

        long start = origin.asLong();
        visited.add(start);
        queue.add(start);
        int maxBlocks = ConfigClient.MAX_CHECK_BLOCKS.get();     // volume cap, e.g. 400
        int maxRadius = ConfigClient.MAX_CHECK_RADIUS.get();

        int head = 0;
        while (head < queue.size()) {
            cur.set(queue.getLong(head++));
            BlockState curState = level.getBlockState(cur);

            for (Direction dir : DIRS) {
                // Leaving through a sealed face? Then nothing to do in this direction.
                if (curState.isFaceSturdy(level, cur, dir)) continue;

                next.setWithOffset(cur, dir);

                // Escape conditions: the space is not a bounded room
                if (level.isOutsideBuildHeight(next)) return Result.OPEN;
                if (!level.hasChunkAt(next)) return Result.OPEN;
                if (Math.abs(next.getX() - origin.getX()) > maxRadius
                        || Math.abs(next.getY() - origin.getY()) > maxRadius
                        || Math.abs(next.getZ() - origin.getZ()) > maxRadius) {
                    return Result.OPEN;
                }

                long nextKey = next.asLong();
                if (visited.contains(nextKey)) continue;

                // Entering through a sealed face on the other side?
                BlockState nextState = level.getBlockState(next);
                if (nextState.isFaceSturdy(level, next, dir.getOpposite())) continue;

                if (visited.size() >= maxBlocks) return Result.OPEN;

                visited.add(nextKey);
                queue.add(nextKey);
            }
        }
        return new Result(true, visited.size());
    }
}
