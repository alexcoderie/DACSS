import blackboard.ChairBlackboard;
import control.Control;
import knowledge_source.*;

public class Main {
    public static void main(String[] args) {
        ChairBlackboard chairBlackboard = new ChairBlackboard();
        Control control = new Control();

        control.addKnowledgeSource(new CutSeat());
        control.addKnowledgeSource(new AssembleFeet());
        control.addKnowledgeSource(new AssembleFeet());
        control.addKnowledgeSource(new AssembleBackrest());
        control.addKnowledgeSource(new AssembleStabilizerBar());
        control.addKnowledgeSource(new PackageChair());

        control.activateKnowledgeSource(chairBlackboard);
    }
}