package ch.usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Pattern {

    int id;

    boolean rooted;
    boolean nonLocal;

    String value;

    @Override
    public String toString() {
        return value;
    }
}
