package com.alibaba.middleware.race.sync.server2.unitTest;

import com.alibaba.middleware.race.sync.server2.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by yche on 6/19/17.
 */
public class APIUsageDemo {
    public static void main(String[] args) throws IOException, InterruptedException {
        PipelinedComputation.FindResultListener findResultListener = new PipelinedComputation.FindResultListener() {
            @Override
            public void sendToClient(String result) {
            }
        };

        Thread.sleep(5000);
        long startTime = System.currentTimeMillis();
        String srcFolder = "/tmp";
        ArrayList<String> filePathList = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            filePathList.add(srcFolder + File.separator + i + ".txt");
        }
        PipelinedComputation.globalComputation(filePathList, findResultListener, 100000, 2000000);

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("/tmp/yche_me.txt"));
        for (String line : PipelinedComputation.finalResultMap.values()) {
            bufferedWriter.write(line);
            bufferedWriter.newLine();
        }
        bufferedWriter.close();
        long endTime = System.currentTimeMillis();
        System.out.println("total time:" + (endTime - startTime) + " ms");

        System.out.println(PipelinedComputation.restoreComputation.inRangeRecordSet.size());
        System.out.println("max len byte[]:" + Arrays.toString(RecordScanner.maxLens));
        System.out.println("min len byte[]:" + Arrays.toString(RecordScanner.minLens));
        System.out.println(RecordScanner.minSKip + ", " + RecordScanner.maxSkip);

        System.out.println("insert:" + InsertOperation.count);
        System.out.println("delete:" + DeleteOperation.count);
        System.out.println("update:" + UpdateOperation.count);
        System.out.println("update pk:" + UpdateKeyOperation.count);
    }
}