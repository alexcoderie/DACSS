package knowledge_source;

public interface KnowledgeSource<BLACKBOARD> {
    boolean execCondition(BLACKBOARD blackboard);
    void execAction(BLACKBOARD blackboard);
}
