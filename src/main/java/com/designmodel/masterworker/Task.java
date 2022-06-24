package com.designmodel.masterworker;


import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public abstract class Task<R> {

    private Consumer<Task<R>> resultAction;

    private static AtomicInteger no = new AtomicInteger(1);

    private Integer workerId;

    private Integer id;

    private R result;

    public Task() {
        this.id = no.getAndIncrement();
    }

    public Integer getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Integer workerId) {
        this.workerId = workerId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public R getResult() {
        return result;
    }

    public void setResult(R result) {
        this.result = result;
    }

    public void setResultAction(Consumer<Task<R>> resultAction) {
        this.resultAction = resultAction;
    }

    public void execute() {
        this.result = this.doExecute();
        resultAction.accept(this);
    }

    protected abstract R doExecute();
}
