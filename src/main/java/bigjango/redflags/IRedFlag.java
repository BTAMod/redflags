package bigjango.redflags;

public interface IRedFlag {
    public abstract boolean isCorrect();
    public abstract void setCorrect(boolean correct);
    public abstract boolean shouldRedraw();
    public abstract void setRedraw(boolean redraw);
}
