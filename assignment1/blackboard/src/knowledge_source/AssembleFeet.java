package knowledge_source;

import blackboard.ChairBlackboard;

public class AssembleFeet implements KnowledgeSource<ChairBlackboard> {
    @Override
    public boolean execCondition(ChairBlackboard chairBlackboard) {
        return chairBlackboard.isCutSeat() && !chairBlackboard.isAssembleFeet();
    }

    @Override
    public void execAction(ChairBlackboard chairBlackboard) {
        chairBlackboard.assembleFeet();
    }
}
