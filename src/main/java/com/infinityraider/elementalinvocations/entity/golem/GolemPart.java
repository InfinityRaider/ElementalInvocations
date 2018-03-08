package com.infinityraider.elementalinvocations.entity.golem;

import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GolemPart implements Comparable<GolemPart> {
    private final EntityGolem golem;
    private final Map<Integer, Vector3d[]> positions;
    private int cap;
    private List<EntityGolemBlock> blocks;

    protected GolemPart(EntityGolem golem) {
        this.golem = golem;
        this.positions = new HashMap<>();
        this.blocks = new ArrayList<>();
        this.cap = this.getMin() + (this.getMax() - this.getMin())*golem.getPotency() / 15;
        this.initPositions(this.positions);
    }

    public abstract int getMin();

    public abstract int getMax();

    public int getCap() {
        return this.cap;
    }

    public int getBlockCount() {
        return this.blocks.size();
    }

    public int excess() {
        return this.getBlockCount() - this.getMin();
    }

    public int blocksNeeded() {
        int excess = excess();
        return excess < 0 ? -excess : 0;
    }

    public boolean canRemoveBlock() {
        return this.excess() > 0;
    }

    public boolean canAddBlock() {
        return this.getBlockCount() < this.getCap();
    }

    public EntityGolemBlock removeBlock() {
        if(this.canRemoveBlock()) {
            return this.blocks.remove(this.getBlockCount() - 1);
        } else {
            return null;
        }
    }

    public boolean addBlock(EntityGolemBlock block) {
        if(this.canAddBlock() && !this.blocks.contains(block)) {
            this.blocks.add(block);
            return true;
        } else {
            return false;
        }
    }

    protected Vector3d[] getBlockPositions() {
        return this.positions.get(Math.max(this.getMin(), this.getBlockCount()));
    }

    protected abstract void initPositions(Map<Integer, Vector3d[]> positions);

    @Override
    public int compareTo(GolemPart other) {
        return this.excess() - other.excess();
    }

    public static List<GolemPart> createParts(EntityGolem golem) {
        List<GolemPart> parts = new ArrayList<>();
        parts.add(new Parts.Body(golem));
        parts.add(new Parts.Arm(golem));
        parts.add(new Parts.LegLeft(golem));
        parts.add(new Parts.LegRight(golem));
        return parts;
    }

    private static class Parts {
        private static class Body extends GolemPart {
            private final Vector3d top_left = new Vector3d(0, 0.8, -0.8);
            private final Vector3d top_mid = new Vector3d(0, 0.8, 0);
            private final Vector3d top_right = new Vector3d(0, 0.8, 0.8);
            private final Vector3d bot_left = new Vector3d(0, 0, -0.4);
            private final Vector3d bot_right = new Vector3d(0, 0, 0.4);
            private final Vector3d front1 = new Vector3d(-0.5, 0.4, 0);
            private final Vector3d back1 = new Vector3d(0.5, 0.4, 0);
            private final Vector3d front2_1 = new Vector3d(-0.5, 0.4, -0.4);
            private final Vector3d front2_2 = new Vector3d(-0.5, 0.4, 0.4);
            private final Vector3d back2_1 = new Vector3d(0.5, 0.4, -0.4);
            private final Vector3d back2_2 = new Vector3d(0.5, 0.4, 0.4);
            private final Vector3d front3_1 = new Vector3d(-0.5, 0, 0);
            private final Vector3d front3_2 = new Vector3d(-0.5, 0.8, -0.4);
            private final Vector3d front3_3 = new Vector3d(-0.5, 0.8, 0.4);
            private final Vector3d back3_1 = new Vector3d(0.5, 0, 0);
            private final Vector3d back3_2 = new Vector3d(0.5, 0.8, -0.4);
            private final Vector3d back3_3 = new Vector3d(0.5, 0.8, 0.4);
            private final Vector3d head1 = new Vector3d(0, 1.3, 0);
            private final Vector3d head2_1 = new Vector3d(0, 1.3, -0.5);
            private final Vector3d head2_2 = new Vector3d(0, 1.3, 0.5);

            Body(EntityGolem golem) {
                super(golem);
            }

            @Override
            public int getMin() {
                return 5;
            }

            @Override
            public int getMax() {
                return 13;
            }

            @Override
            protected void initPositions(Map<Integer, Vector3d[]> positions) {
                positions.put(5, new Vector3d[] {top_left, top_mid, top_right, bot_left, bot_right});
                positions.put(6, new Vector3d[] {top_left, top_mid, top_right, bot_left, bot_right, front1});
                positions.put(7, new Vector3d[] {top_left, top_mid, top_right, bot_left, bot_right, front1, back1});
                positions.put(8, new Vector3d[] {top_left, top_mid, top_right, bot_left, bot_right, front2_1, front2_2, back1});
                positions.put(9, new Vector3d[] {top_left, top_mid, top_right, bot_left, bot_right, front2_1, front2_2, back2_1, back2_2});
                positions.put(10, new Vector3d[] {top_left, top_mid, top_right, bot_left, bot_right, front3_1, front3_2, front3_3, back2_1, back2_1});
                positions.put(11, new Vector3d[] {top_left, top_mid, top_right, bot_left, bot_right, front3_1, front3_2, front3_3, back3_1, back3_2, back3_3});
                positions.put(12, new Vector3d[] {top_left, top_mid, top_right, bot_left, bot_right, front3_1, front3_2, front3_3, back3_1, back3_2, back3_3, head1});
                positions.put(13, new Vector3d[] {top_left, top_mid, top_right, bot_left, bot_right, front3_1, front3_2, front3_3, back3_1, back3_2, back3_3, head2_1, head2_2});
            }
        }

        private static class Arm extends GolemPart {
            private final Vector3d top = new Vector3d(0, 0, 0);
            private final Vector3d mid = new Vector3d(0, -0.8, 0);
            private final Vector3d bottom = new Vector3d(0, -1.6, 0);
            private final Vector3d fist1 = new Vector3d(0, -2.1, 0);
            private final Vector3d fist2_1 = new Vector3d(-0.5, -2.1, 0);
            private final Vector3d fist2_2 = new Vector3d(0.5, -2.1, 0);
            private final Vector3d fist3_1 = new Vector3d(-0.5, -2.1, 0.5);
            private final Vector3d fist3_2 = new Vector3d(-0.5, -2.1, -0.5);
            private final Vector3d fist3_3 = new Vector3d(0.5, -2.1, 0);
            private final Vector3d fist4 = new Vector3d(0, -2.6, 0);

            Arm(EntityGolem golem) {
                super(golem);
            }

            @Override
            public int getMin() {
                return 3;
            }

            @Override
            public int getMax() {
                return 7;
            }

            @Override
            protected void initPositions(Map<Integer, Vector3d[]> positions) {
                positions.put(3, new Vector3d[] {top, mid, bottom});
                positions.put(4, new Vector3d[] {top, mid, bottom, fist1});
                positions.put(5, new Vector3d[] {top, mid, bottom, fist2_1, fist2_2});
                positions.put(6, new Vector3d[] {top, mid, bottom, fist3_1, fist3_2, fist3_3});
                positions.put(7, new Vector3d[] {top, mid, bottom, fist3_1, fist3_2, fist3_3, fist4});
            }
        }

        private static class Leg extends GolemPart {
            private final Vector3d top = new Vector3d(0, 0, 0);
            private final Vector3d mid = new Vector3d(0, -0.8, 0);
            private final Vector3d bottom = new Vector3d(0, -1.6, 0);
            private final Vector3d foot1 = new Vector3d(-0.5, -1.6, 0);
            private final Vector3d foot2_1 = new Vector3d(-0.5, -1.6, 0.5);
            private final Vector3d foot2_2 = new Vector3d(-0.5, -1.6, -0.5);
            private final Vector3d foot3 = new Vector3d(0.5, -1.6, 0);

            Leg(EntityGolem golem) {
                super(golem);
            }

            @Override
            public int getMin() {
                return 3;
            }

            @Override
            public int getMax() {
                return 6;
            }

            protected void initPositions(Map<Integer, Vector3d[]> positions) {
                positions.put(3, new Vector3d[] {top, mid, bottom});
                positions.put(4, new Vector3d[] {top, mid, bottom, foot1});
                positions.put(5, new Vector3d[] {top, mid, bottom, foot2_1, foot2_2});
                positions.put(6, new Vector3d[] {top, mid, bottom, foot2_1, foot2_2, foot3});
            }
        }

        private static class LegLeft extends Leg {
            LegLeft(EntityGolem golem) {
                super(golem);
            }
        }

        private static class LegRight extends Leg {
            LegRight(EntityGolem golem) {
                super(golem);
            }
        }
    }
}