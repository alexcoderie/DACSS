package knowledge_source;

import blackboard.ChairBlackboard;

public class PackageChair implements KnowledgeSource<ChairBlackboard> {
    @Override
    public boolean execCondition(ChairBlackboard chairBlackboard) {
        return chairBlackboard.isCutSeat() &&
                chairBlackboard.isAssembleFeet() &&
                chairBlackboard.isAssembleBackrest() &&
                chairBlackboard.isAssembleStabilizer() &&
                !chairBlackboard.isPackageChair();
    }

    @Override
    public void execAction(ChairBlackboard chairBlackboard) {
        chairBlackboard.packageChair();
    }
}
