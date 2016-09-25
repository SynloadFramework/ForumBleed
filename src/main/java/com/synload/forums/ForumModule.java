package com.synload.forums;

import com.synload.framework.modules.ModuleClass;
import com.synload.framework.modules.annotations.Module;

/**
 * Created by Nathaniel on 9/24/2016.
 */
@Module(name="Forum Module", author="Nathaniel Davidson", version="0.1", depend = {}, log= Module.LogLevel.INFO)
public class ForumModule extends ModuleClass {

    @Override
    public void initialize() {

    }

    @Override
    public void crossTalk(Object... objects) {

    }
}
