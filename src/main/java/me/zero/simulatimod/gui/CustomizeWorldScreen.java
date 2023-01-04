package me.zero.simulatimod.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
public class CustomizeWorldScreen extends Screen {
    private final Screen parent;
    private ButtonListWidget listWidget;

    public CustomizeWorldScreen (Screen parentScreen) {
        super(Text.translatable("simulatimod.customize_world_screen"));
        this.parent = parentScreen;
    }

    @Override
    protected void init() {
        this.addDrawable(ButtonWidget.builder(Text.translatable("simulatimod.customize_world_screen.done"), button -> this.client.setScreen(parent)).dimensions(this.width / 2 + 98, this.height - 27, 90, 20).build());
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        this.renderBackground(matrices);
    }
}
