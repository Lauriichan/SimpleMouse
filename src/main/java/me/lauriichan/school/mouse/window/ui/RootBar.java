package me.lauriichan.school.mouse.window.ui;

import me.lauriichan.school.mouse.window.ui.component.bar.BarBox;
import me.lauriichan.school.mouse.window.util.IBoxRenderer;

public abstract class RootBar extends TriggerBar<BarBox> {

    public abstract BarBox createBox(IBoxRenderer renderer);

}
