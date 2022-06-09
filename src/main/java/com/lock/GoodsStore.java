package com.lock;

import com.lock.model.GoodsVO;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GoodsStore {

    private static DataBuffer<GoodsVO> notSafeDataBuffer = new NotSafeDataBuffer<>();

    private static DataBuffer<GoodsVO> safeDataBuffer = new SafeDataBuffer<>();

    private static final boolean useSafe = true;

    private static Callable<GoodsVO> producerAction = () -> {
        GoodsVO goodsVO = GoodsVO.produceOne();
        if (useSafe) {
            safeDataBuffer.add(goodsVO);
        } else {
            notSafeDataBuffer.add(goodsVO);
        }
        return goodsVO;
    };

    private static Callable<GoodsVO> consumerAction = () -> {
        GoodsVO goodsVO;
        if (useSafe) {
            goodsVO = safeDataBuffer.fetchOne();
        } else {
            goodsVO = notSafeDataBuffer.fetchOne();
        }
        return goodsVO;
    };

    public static void main(String[] args) {
        int T_CT = 20;

        ExecutorService service = Executors.newFixedThreadPool(T_CT);
        for (int i = 0; i < 5; i++) {
            service.submit(new Producer(producerAction, 1000));
            service.submit(new Consumer(consumerAction, 4500));
        }
    }
}
