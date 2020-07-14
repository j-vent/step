// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
 
package com.google.sps;
 
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
 
 
public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
     List<TimeRange> fullDay = new ArrayList<TimeRange>();
    // List<TimeRange> fullDay = new Set<TimeRange>();
    fullDay.add(TimeRange.WHOLE_DAY);
   

    List<TimeRange> availableTimes = new ArrayList<TimeRange>();
    
    // throw new UnsupportedOperationException("TODO: Implement this method.");
    
    Collection<String> attendees = request.getAttendees();
 
    // this is some horrific n^3 runtime

    // cannot modify arraylist in an enhanced for loop, must create separate arraylists
    List<TimeRange> freeTimes = new ArrayList<TimeRange>();
    List<TimeRange> blockedTimes = new ArrayList<TimeRange>();
    TimeRange freetime;
    for(Event event: events){
        Set<String> eventAttendees = event.getAttendees();
        for(String attendee:eventAttendees){
            if(attendees.contains(attendee)){
              TimeRange blocked = event.getWhen();
              int start = blocked.start();
              int end = blocked.end();
            
              for(TimeRange timeslot: fullDay){
                if(blocked.overlaps(timeslot)){
                     // top overlap: add a timeslot before this event
                    if(start >= timeslot.start() && start < timeslot.end()){
                      freetime = TimeRange.fromStartEnd(timeslot.start(), start,false);
                      if(!fullDay.contains(freetime)){
                        freeTimes.add(freetime);
                      }
                     
                    }
                    if(end >= timeslot.start() && end < timeslot.end()){
                      // bottom overlap: add a timeslot after this event
                      freetime = TimeRange.fromStartEnd(end,timeslot.end(),false);
                      if(!fullDay.contains(freetime)){
                        freeTimes.add(freetime);
                      }
                    }
                    blockedTimes.add(timeslot);
                }
              }
              fullDay.removeAll(blockedTimes);
              fullDay.addAll(freeTimes);

                
            }
        }
    }
    //System.out.println("fullday");
    //System.out.println(fullDay);
    // get all the remaining, free time slots
    
    // List<TimeRange> availableTimes = new ArrayList<TimeRange>();
    return fullDay;
  }
}
 
 
 

