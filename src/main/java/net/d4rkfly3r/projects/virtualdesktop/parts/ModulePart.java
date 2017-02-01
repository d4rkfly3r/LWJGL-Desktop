package net.d4rkfly3r.projects.virtualdesktop.parts;

import java.util.ArrayList;
import java.util.List;

public abstract class ModulePart extends BasePart {

    private final List<ComponentPart> componentList;

    public ModulePart() {
        this.componentList = new ArrayList<>();
    }

    public ModulePart addComponent(final ComponentPart iComponentPart) {
        this.componentList.add(iComponentPart);
        return this;
    }
}
