package chair;

public class Chair {
    private boolean isCutSeat;
    private boolean isAssembleFeet;
    private boolean isAssembleBackrest;
    private boolean isAssembleStabilizer;
    private boolean isPackageChair;

    public Chair() {
        this.isCutSeat = false;
        this.isAssembleFeet = false;
        this.isAssembleBackrest = false;
        this.isAssembleStabilizer = false;
        this.isPackageChair = false;
    }

    public void cutSeat() {
        this.isCutSeat = true;
    }

    public boolean isCutSeat() {
        return this.isCutSeat;
    }
    public void assembleFeet() {
        this.isAssembleFeet = true;
    }

    public boolean isAssembleFeet() {
        return this.isAssembleFeet;
    }
    public void assembleBackrest() {
        this.isAssembleBackrest = true;
    }

    public boolean isAssembleBackrest() {
        return isAssembleBackrest;
    }
    public void assembleStabilizer() {
        this.isAssembleStabilizer = true;
    }
    public boolean isAssembleStabilizer() {
        return isAssembleStabilizer;
    }

    public void packageChair() {
        this.isPackageChair = true;
    }
    public boolean isPackageChair() {
        return isPackageChair;
    }
}
