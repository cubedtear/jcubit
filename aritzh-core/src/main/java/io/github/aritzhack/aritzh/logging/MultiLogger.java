/*
 * Copyright 2015 Aritz Lopez
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.github.aritzhack.aritzh.logging;

import com.google.common.collect.Sets;
import io.github.aritzhack.aritzh.logging.core.ALogger;
import io.github.aritzhack.aritzh.logging.core.ILogger;

import java.util.Set;

/**
 * @author Aritz Lopez
 */
public class MultiLogger extends ALogger {

    private final Set<ILogger> loggers;

    public MultiLogger(ILogger... loggers) {
        this.loggers = Sets.newHashSet(loggers);
    }

    @Override
    public void t(String msg) {
        for (ILogger l : this.loggers) {
            l.t(msg);
        }
    }

    @Override
    public void t(String msg, Throwable t) {
        for (ILogger l : this.loggers) {
            l.t(msg, t);
        }
    }

    @Override
    public void d(String msg) {
        for (ILogger l : this.loggers) {
            l.d(msg);
        }
    }

    @Override
    public void d(String msg, Throwable t) {
        for (ILogger l : this.loggers) {
            l.d(msg, t);
        }
    }

    @Override
    public void i(String msg) {
        for (ILogger l : this.loggers) {
            l.i(msg);
        }
    }

    @Override
    public void i(String msg, Throwable t) {
        for (ILogger l : this.loggers) {
            l.i(msg, t);
        }
    }

    @Override
    public void w(String msg) {
        for (ILogger l : this.loggers) {
            l.w(msg);
        }
    }

    @Override
    public void w(String msg, Throwable t) {
        for (ILogger l : this.loggers) {
            l.w(msg, t);
        }
    }

    @Override
    public void e(String msg) {
        for (ILogger l : this.loggers) {
            l.e(msg);
        }
    }

    @Override
    public void e(String msg, Throwable t) {
        for (ILogger l : this.loggers) {
            l.e(msg, t);
        }
    }
}
