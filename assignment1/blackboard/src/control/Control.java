package control;

import blackboard.ChairBlackboard;
import knowledge_source.KnowledgeSource;

import java.util.ArrayList;
import java.util.List;

public class Control {
    private List<KnowledgeSource> knowledgeSourceList;

    public Control() {
        knowledgeSourceList = new ArrayList<>();
    }

    public void addKnowledgeSource(KnowledgeSource ks) {
        knowledgeSourceList.add(ks);
    }

    public void activateKnowledgeSource(ChairBlackboard chairBlackboard) {
        while(!chairBlackboard.isPackageChair()) {
            for(KnowledgeSource ks : knowledgeSourceList) {
                if(ks.execCondition(chairBlackboard))
                    ks.execAction(chairBlackboard);
            }
        }
    }
}
