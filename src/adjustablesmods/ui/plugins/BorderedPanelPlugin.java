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

    @Override
    public void positionChanged(PositionAPI position) {
        this.position = position;
    }

    @Override
    public void renderBelow(float alphaMult) {
        if (this.renderBackground && this.position != null) {
            Color c = this.backgroundColor;

            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_CULL_FACE);


            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            GL11.glColor4f(c.getRed() / 255f,
                    c.getGreen() / 255f,
                    c.getBlue() / 255f,
                    c.getAlpha() / 255f * (alphaMult * this.alpha));

            GL11.glRectf(this.position.getX(), this.position.getY(), this.position.getX() + this.position.getWidth(), this.position.getY() + this.position.getHeight());

            GL11.glPopMatrix();
        }
    }

    @Override
    public void render(float alphaMult) {
        if (this.position != null) {
            Color c = this.borderColor;
            GL11.glPushMatrix();

            GL11.glTranslatef(0f, 0f, 0f);
            GL11.glRotatef(0f, 0f, 0f, 1f);

            GL11.glDisable(GL11.GL_TEXTURE_2D);


            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);


            GL11.glColor4f(c.getRed() / 255f,
                    c.getGreen() / 255f,
                    c.getBlue() / 255f,
                    c.getAlpha() / 255f * (alphaMult * this.alpha));

            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glBegin(GL11.GL_LINE_STRIP);

            GL11.glVertex2f(this.position.getX(), this.position.getY());
            GL11.glVertex2f(this.position.getX(), this.position.getY() + this.position.getHeight());
            GL11.glVertex2f(this.position.getX() + this.position.getWidth(), this.position.getY() + this.position.getHeight());
            GL11.glVertex2f(this.position.getX() + this.position.getWidth(), this.position.getY());
            GL11.glVertex2f(this.position.getX(), this.position.getY());

            GL11.glEnd();
            GL11.glPopMatrix();
        }
    }
}
