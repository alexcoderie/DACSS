package knowledge_source;

import blackboard.ChairBlackboard;

public class AssembleBackrest implements KnowledgeSource<ChairBlackboard> {
    @Override
    public boolean execCondition(ChairBlackboard chairBlackboard) {
        return chairBlackboard.isCutSeat() && !chairBlackboard.isAssembleBackrest();
    }

    @Override
    public void execAction(ChairBlackboard chairBlackboard) {
        chairBlackboard.assembleBackrest();
    }
}
