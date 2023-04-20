package usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InputEdit {

    int startByte;
    int oldEndByte;
    int newEndByte;
    Point startPoint;
    Point oldEndPoint;
    Point newEndPoint;
}
