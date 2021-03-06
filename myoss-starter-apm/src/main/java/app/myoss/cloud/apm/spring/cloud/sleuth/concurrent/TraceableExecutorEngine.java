/*
 * Copyright 2018-2018 https://github.com/myoss
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package app.myoss.cloud.apm.spring.cloud.sleuth.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.cloud.sleuth.instrument.async.TraceableExecutorService;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import app.myoss.cloud.core.lang.concurrent.ExecutorEngine;

/**
 * 多线程执行框架，可以追踪调用链的多线程池
 *
 * @author Jerry.Chen
 * @since 2018年8月30日 上午9:58:38
 * @see app.myoss.cloud.core.lang.concurrent.ExecutorEngine
 */
public class TraceableExecutorEngine {
    /**
     * 创建可以追踪调用链的多线程池
     *
     * @param beanFactory Spring BeanFactory
     * @param delegate 代理的线程池
     * @return 可以追踪调用链的多线程池执行框架
     */
    public static ExecutorEngine buildTraceableExecutorService(BeanFactory beanFactory, ExecutorService delegate) {
        TraceableExecutorService traceableExecutorService = new TraceableExecutorService(beanFactory, delegate);
        return new ExecutorEngine(traceableExecutorService);
    }

    /**
     * 创建可以追踪调用链的多线程池
     *
     * @param beanFactory Spring BeanFactory
     * @return 可以追踪调用链的多线程池执行框架
     */
    public static ExecutorEngine buildTraceableExecutorService(BeanFactory beanFactory) {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setDaemon(true)
                .setNameFormat("TraceableExecutorEngineThreadPool-%d")
                .build();
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5, 200, 0, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), threadFactory);
        return buildTraceableExecutorService(beanFactory, poolExecutor);
    }
}
