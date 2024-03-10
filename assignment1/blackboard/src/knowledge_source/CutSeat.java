package knowledge_source;

import blackboard.ChairBlackboard;

public class CutSeat implements KnowledgeSource<ChairBlackboard> {
    @Override
    public boolean execCondition(ChairBlackboard chairBlackboard) {
        return !chairBlackboard.isCutSeat();
    }

    @Override
    public void execAction(ChairBlackboard chairBlackboard) {
        chairBlackboard.cutSeat();
    }
}
