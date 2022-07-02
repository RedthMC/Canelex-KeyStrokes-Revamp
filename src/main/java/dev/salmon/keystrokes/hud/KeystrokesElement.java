package dev.salmon.keystrokes.hud;

import cc.polyfrost.oneconfig.config.annotations.Color;
import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.gui.OneConfigGui;
import cc.polyfrost.oneconfig.hud.Hud;
import cc.polyfrost.oneconfig.libs.universal.UGraphics;
import cc.polyfrost.oneconfig.libs.universal.UMatrixStack;
import cc.polyfrost.oneconfig.platform.Platform;
import dev.salmon.keystrokes.Keystrokes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;

public class KeystrokesElement extends Hud {

    @Switch(
            name = "Enable Mouse Keystrokes"
    )
    public boolean mouseKeystrokes = true;
    @Switch(
            name = "Jump (Space) Keystrokes"
    )
    public boolean jumpKeystrokes = false;

    @Color(
            name = "Unpressed Background Color"
    )
    public OneColor bgUnpressed = new OneColor(-922746880);

    @Color(
            name = "Unpressed Text Color"
    )
    public OneColor textUnpressed = new OneColor(-1);

    @Color(
            name = "Pressed Background Color"
    )
    public OneColor bgPressed = new OneColor(-905969665);

    @Color(
            name = "Pressed Text Color"
    )
    public OneColor textPressed = new OneColor(-16777216);

    @Slider(
            name = "Fading Time", min = 1F, max = 250F, step = 1)
    public int fadingTime = 100;

    @Switch(
            name = "Chroma"
    )
    public boolean chroma = false;

    @Switch(
            name = "Shadow"
    )
    public boolean shadow = false;

    transient private final GuiKey[] movementKeys;
    transient private final GuiKey[] mouseKeys;
    transient private final GuiKey jumpKey;

    public KeystrokesElement() {
        super(true, 0, 0, 1, false, 2, 0, 0, new OneColor(0, 0, 0, 120), false, 2, new OneColor(0, 0, 0));
        GameSettings gs = Minecraft.getMinecraft().gameSettings;
        (this.movementKeys = new GuiKey[4])[0] = new GuiKey(20, 0, 19, 19, gs.keyBindForward);
        this.movementKeys[1] = new GuiKey(0, 20, 19, 19, gs.keyBindLeft);
        this.movementKeys[2] = new GuiKey(20, 20, 19, 19, gs.keyBindBack);
        this.movementKeys[3] = new GuiKey(40, 20, 19, 19, gs.keyBindRight);
        (this.mouseKeys = new GuiKey[2])[0] = new GuiKey(0, 40, 29, 19, gs.keyBindAttack); // left click
        this.mouseKeys[1] = new GuiKey(30, 40, 29, 19, gs.keyBindUseItem); // right click
        this.jumpKey = new GuiKeySpace(0, 60, 59, 11, gs.keyBindJump); // space
    }

    @Override
    public void drawAll(UMatrixStack matrices, float x, float y, float scale, boolean background) {
        if (!showInGuis && Platform.getGuiPlatform().getCurrentScreen() != null && !(Platform.getGuiPlatform().getCurrentScreen() instanceof OneConfigGui)) return;
        if (!showInChat && Platform.getGuiPlatform().isInChat()) return;
        if (!showInDebug && Platform.getGuiPlatform().isInDebug()) return;
        if (this.scale != 0.0F && enabled && Keystrokes.INSTANCE.config.enabled) {
            UGraphics.GL.pushMatrix();
            UGraphics.GL.translate(-x * (scale - 1.0F), -y * (scale - 1.0F), 0.0F);
            UGraphics.GL.scale(scale, scale, 1.0F);
            for (GuiKey key : this.movementKeys) {
                key.updateKeyState();
                key.drawKey(x, y, scale);
            }
            if (mouseKeystrokes) {
                for (GuiKey key : this.mouseKeys) {
                    key.updateKeyState();
                    key.drawKey(x, y, scale);
                }
            }
            if (jumpKeystrokes) {
                if (mouseKeystrokes) {
                    jumpKey.relY = 60;
                } else {
                    jumpKey.relY = 40;
                }
                this.jumpKey.updateKeyState();
                this.jumpKey.drawKey(x, y, scale);
            }
            UGraphics.GL.popMatrix();
        }
    }

    @Override
    public void drawExampleAll(UMatrixStack matrices, float x, float y, float scale, boolean background) {
        drawAll(matrices, x, y, scale, background);
    }

    @Override
    public void draw(UMatrixStack matrices, int x, int y, float scale) {
        // no-op, not used
    }

    @Override
    public int getWidth(float scale) {
        return (int) (59 * scale);
    }

    @Override
    public int getHeight(float scale) {
        int height = 40;
        if (mouseKeystrokes)
            height += 20;
        if (jumpKeystrokes)
            height += 12;
        return (int) (height * scale);
    }
}