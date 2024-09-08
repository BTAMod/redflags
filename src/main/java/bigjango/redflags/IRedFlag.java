package bigjango.redflags;

public interface IRedFlag {
    public abstract boolean isCorrect();
    public abstract void setCorrect(boolean correct);
    public abstract int getTexture();
    public abstract void setTexture(int t);
}
