package net.d4rkfly3r.projects.virtualdesktop.parts;

import java.util.ArrayList;
import java.util.List;

public abstract class WindowPart extends BasePart {

    protected final List<BasePart> partList;
    protected String title;

    protected WindowPart() {
        this.partList = new ArrayList<>();
    }

    public WindowPart addComponent(final ComponentPart iComponentPart) {
        this.partList.add(iComponentPart);
        return this;
    }

    public WindowPart addModule(final ModulePart modulePart) {
        this.partList.add(modulePart);
        return this;
    }

    @Override
    public void render() {
        this.partList.forEach(BasePart::render);
    }

    public String getTitle() {
        return this.title;
    }

    public WindowPart setTitle(final String title) {
        this.title = title;
        return this;
    }

    @Override
    public void mouseClicked(int x, int y, int button) {

    }

    @Override
    public void mouseReleased(int x, int y, int button) {

    }

    @Override
    public BasePart revalidate() {
        return this;
    }

    @Override
    public void mouseDrag(int x, int y, int button) {
    }
}
