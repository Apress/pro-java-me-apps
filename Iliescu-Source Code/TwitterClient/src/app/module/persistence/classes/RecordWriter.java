package app.module.persistence.classes;

public interface RecordWriter
{
    public void writeInt ( int value );

    public void writeLong ( long value );

    public void writeString ( String value );

    public void writeBoolean ( boolean value ) ;

    public void writeByte( byte value );

    public void reset();

    public byte[] getCurrentResult();
}
