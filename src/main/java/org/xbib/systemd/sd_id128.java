package org.xbib.systemd;

import org.bridj.BridJ;
import org.bridj.Pointer;
import org.bridj.StructObject;
import org.bridj.ann.Array;
import org.bridj.ann.Field;
import org.bridj.ann.Library;
import org.bridj.ann.Union;

@Union
@Library("systemd")
public class sd_id128 extends StructObject {

    static {
        BridJ.register();
    }

    @Array({16})
    @Field(0)
    public Pointer<Byte > bytes() {
        return io.getPointerField(this, 0);
    }

    @Array({2})
    @Field(1)
    public Pointer<Long > qwords() {
        return io.getPointerField(this, 1);
    }

    public sd_id128() {
        super();
    }

    public sd_id128(Pointer pointer) {
        super(pointer);
    }
}
