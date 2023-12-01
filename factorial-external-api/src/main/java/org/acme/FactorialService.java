package org.acme;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class FactorialService {

 Logger LOGGER = LoggerFactory.getLogger(FactorialService.class);
 
 public Uni<Response> getFactorialValue(int input, String uuid) {
        long startTime = System.currentTimeMillis();
        long output = calculateFactorialValue(input);

        // GENERATE A RANDOM NUMBER TO SELECT SLEEP TIME
        int sleepTime = 0;
        if((getRandomNumber(1,10)%2)==0){
            sleepTime = getRandomNumber(1,3) * 1000; // Sleep <1000,3000> MS for SLOW Request
        } else {
            sleepTime = 10; // Sleep 10 MS for FAST Request
        }

        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
         LOGGER.error("Error while Thread sleep process for uuid [{}] ", uuid,e);
            Response errorResponse = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error while Thread sleep process for uuid "+uuid).build();
            return Uni.createFrom().item(errorResponse);
        }

        long endTime = System.currentTimeMillis();
        long timeElapsed = endTime - startTime;
        
        Factorial factorial = new Factorial(input, output, timeElapsed+" MS", uuid);
        Response response = Response.ok(factorial).build();
        LOGGER.info("END PROCESS, input [{}] output [{}] timeElapsed [{} MS] uuid [{}]", input, output, timeElapsed, uuid);
        
        return Uni.createFrom().item(response);
    }

 private long calculateFactorialValue(int n) {
        long fact = 1;
        for (int i = 2; i <= n; i++) {
            fact = fact * i;
        }
        return fact;
    }
 
 private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}