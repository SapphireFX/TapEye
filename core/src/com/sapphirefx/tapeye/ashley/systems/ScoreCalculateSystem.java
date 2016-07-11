package com.sapphirefx.tapeye.ashley.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.StringBuilder;
import com.sapphirefx.tapeye.ashley.components.FailsComponent;
import com.sapphirefx.tapeye.ashley.components.LoggingComponent;
import com.sapphirefx.tapeye.ashley.components.MessageComponent;
import com.sapphirefx.tapeye.ashley.components.ScoreComponent;
import com.sapphirefx.tapeye.ashley.components.TimerLabelComponent;
import com.sapphirefx.tapeye.tools.GameParameters;

/**
 * Created by sapphire on 19.12.2015.
 */
public class ScoreCalculateSystem extends IteratingSystem
{
    public ScoreCalculateSystem()
    {
        super(Family.one(LoggingComponent.class, ScoreComponent.class, FailsComponent.class, MessageComponent.class, TimerLabelComponent.class).get());
    }

    @Override
    public void update(float deltaTime)
    {
        super.update(deltaTime);
        if (!GameParameters.isPaused) GameParameters.timeElapsed += deltaTime;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime)
    {
        int secondsElapsed = (int)GameParameters.timeElapsed;
        StringBuilder stringBuilder;

        ScoreComponent scoreComponent = entity.getComponent(ScoreComponent.class);
        FailsComponent failsComponent = entity.getComponent(FailsComponent.class);
        MessageComponent messageComponent = entity.getComponent(MessageComponent.class);
        TimerLabelComponent timerLabelComponent = entity.getComponent(TimerLabelComponent.class);
        LoggingComponent loggingComponent = entity.getComponent(LoggingComponent.class);
        if (scoreComponent != null)
        {
            stringBuilder = scoreComponent.scoreLabel.text;
            stringBuilder.delete(0, stringBuilder.length());
            stringBuilder.append(GameParameters.score);
        }
        else if (failsComponent != null)
        {
            stringBuilder = failsComponent.failsLabel.text;
            stringBuilder.delete(0, stringBuilder.length());
            stringBuilder.append(GameParameters.fails);
        }
        else if (timerLabelComponent != null)
        {
            stringBuilder = timerLabelComponent.timerLabel.text;
            stringBuilder.delete(0, stringBuilder.length());
            GameParameters.timer = String.valueOf(secondsElapsed/60 + " : " + secondsElapsed%60);
            stringBuilder.append(GameParameters.timer);
        }
        else if (loggingComponent != null)
        {
            stringBuilder = loggingComponent.loggingLabel.text;
            stringBuilder.delete(0, stringBuilder.length());
            stringBuilder.append(GameParameters.logging);
        }
        else if (messageComponent != null)
        {}
    }
}
