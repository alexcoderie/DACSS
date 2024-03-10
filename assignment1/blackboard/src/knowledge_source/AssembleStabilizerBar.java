package knowledge_source;

import blackboard.ChairBlackboard;

public class AssembleStabilizerBar implements KnowledgeSource<ChairBlackboard> {
    @Override
    public boolean execCondition(ChairBlackboard chairBlackboard) {
        return chairBlackboard.isAssembleFeet() && !chairBlackboard.isAssembleStabilizer();
    }

    @Override
    public void execAction(ChairBlackboard chairBlackboard) {
        chairBlackboard.assembleStabilizer();
    }
}
