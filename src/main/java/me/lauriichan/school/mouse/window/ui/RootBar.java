package me.lauriichan.school.mouse.window.ui;

import me.lauriichan.school.mouse.util.IBoxRenderer;
import me.lauriichan.school.mouse.window.ui.component.bar.BarBox;

public abstract class RootBar extends TriggerBar<BarBox> {

    public abstract BarBox createBox(IBoxRenderer renderer);

}
