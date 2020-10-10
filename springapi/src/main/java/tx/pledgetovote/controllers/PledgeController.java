package tx.pledgetovote.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tx.pledgetovote.model.Pledge;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class PledgeController {
    private List<Pledge> pledges = new ArrayList<>();  //Temp storage instead of Database for now.
    private AtomicLong nextId = new AtomicLong();

    @RequestMapping("/home")
    public String welcome(){
        return "Welcome to the Pledge to Vote REST API";
    }

        //creating pledges
        @PostMapping("/pledges")
        @ResponseStatus(HttpStatus.CREATED) //Sends correct 201 http code instead of a 200.
        public Pledge createNewPledge(@RequestBody Pledge pledge){
            //Set pledge to have next ID
            pledge.setId(nextId.incrementAndGet());  //increments the value, returns it after incrementation, creating id

            pledges.add(pledge);
            return pledge;
        }

        //getting pledges previously made.
        @GetMapping("/pledges")
        public List<Pledge> getAllPledges(){
        return pledges;
        }

        //getting pledges that have been previously made using a known pledge Id.
        @GetMapping("/pledges/{id}")
        public Pledge getSinglePledge(@PathVariable("id") Long pledgeId){
            for(Pledge pledge: pledges){
                if (pledge.getId() == pledgeId){
                    return pledge;
                }
            }
            throw new IllegalArgumentException();
        }

        //Changing pledges made.
        @PutMapping("pledges/{id}")
        public Pledge editSinglePledge(@PathVariable("id") Long pledgeId, @RequestBody Pledge newPledge){
            for(Pledge pledge: pledges){
                if (pledge.getId() == pledgeId){
                    pledges.remove(pledge);
                    newPledge.setId(pledgeId);
                    pledges.add(newPledge);
                    return pledge;
                }
            }
            throw new IllegalArgumentException();
        }

        //Create Exception Handler
        @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Request ID not found.")
        @ExceptionHandler(IllegalArgumentException.class)  // Fixes http 500 error when bad id is passed, changes it to a 400 error.
        public void badIdExceptionHandler(){
            //nothing.
        }
}
