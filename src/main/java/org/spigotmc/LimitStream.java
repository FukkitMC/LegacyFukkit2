package org.spigotmc;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.nbt.PositionTracker;

public class LimitStream extends FilterInputStream
{

    private final PositionTracker limit;

    public LimitStream(InputStream is, PositionTracker limit)
    {
        super( is );
        this.limit = limit;
    }

    @Override
    public int read() throws IOException
    {
        limit.add( 8 );
        return super.read();
    }

    @Override
    public int read(byte[] b) throws IOException
    {
        limit.add( b.length * 8 );
        return super.read( b );
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException
    {
        limit.add( len * 8 );
        return super.read( b, off, len );
    }
}
