package product;

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
        System.out.println("Seat was cut");
        this.isCutSeat = true;
    }

    public boolean isCutSeat() {
        return this.isCutSeat;
    }
    public void assembleFeet() {
        System.out.println("Feet were assembled");
        this.isAssembleFeet = true;
    }

    public boolean isAssembleFeet() {
        return this.isAssembleFeet;
    }
    public void assembleBackrest() {
        System.out.println("Backseat was assembled");
        this.isAssembleBackrest = true;
    }

    public boolean isAssembleBackrest() {
        return isAssembleBackrest;
    }
    public void assembleStabilizer() {
        System.out.println("Stabilizer bar was assembled");
        this.isAssembleStabilizer = true;
    }
    public boolean isAssembleStabilizer() {
        return isAssembleStabilizer;
    }

    public void packageChair() {
        System.out.println("Chair was packaged");
        this.isPackageChair = true;
    }
    public boolean isPackageChair() {
        return isPackageChair;
    }
}
