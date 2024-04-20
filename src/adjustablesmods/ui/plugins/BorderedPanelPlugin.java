package adjustablesmods.ui.plugins;

import com.fs.starfarer.api.campaign.BaseCustomUIPanelPlugin;
import com.fs.starfarer.api.ui.PositionAPI;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class BorderedPanelPlugin extends BaseCustomUIPanelPlugin {
    public PositionAPI position = null;
    public boolean renderBackground = false;
    public Color backgroundColor = Misc.getDarkPlayerColor();
    public Color borderColor = Misc.getDarkPlayerColor();
    public float alpha = 1f;

    public BorderedPanelPlugin() {
    }

    public BorderedPanelPlugin(boolean renderBackground, Color backgroundColor, Color borderColor, float alpha) {
        this.renderBackground = renderBackground;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        this.alpha = alpha;
    }

    @Override
    public void positionChanged(PositionAPI position) {
        this.position = position;
    }

    @Override
    public void renderBelow(float alphaMult) {
        if (renderBackground && position != null) {
            Color c = backgroundColor;

            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_CULL_FACE);


            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            GL11.glColor4f(c.getRed() / 255f,
                    c.getGreen() / 255f,
                    c.getBlue() / 255f,
                    c.getAlpha() / 255f * (alphaMult * alpha));

            GL11.glRectf(position.getX(), position.getY(), position.getX() + position.getWidth(), position.getY() + position.getHeight());

            GL11.glPopMatrix();
        }
    }

    @Override
    public void render(float alphaMult) {
        if (position != null) {
            Color c = borderColor;
            GL11.glPushMatrix();

            GL11.glTranslatef(0f, 0f, 0f);
            GL11.glRotatef(0f, 0f, 0f, 1f);

            GL11.glDisable(GL11.GL_TEXTURE_2D);


            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);


            GL11.glColor4f(c.getRed() / 255f,
                    c.getGreen() / 255f,
                    c.getBlue() / 255f,
                    c.getAlpha() / 255f * (alphaMult * alpha));

            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glBegin(GL11.GL_LINE_STRIP);

            GL11.glVertex2f(position.getX(), position.getY());
            GL11.glVertex2f(position.getX(), position.getY() + position.getHeight());
            GL11.glVertex2f(position.getX() + position.getWidth(), position.getY() + position.getHeight());
            GL11.glVertex2f(position.getX() + position.getWidth(), position.getY());
            GL11.glVertex2f(position.getX(), position.getY());

            GL11.glEnd();
            GL11.glPopMatrix();
        }
    }
}
