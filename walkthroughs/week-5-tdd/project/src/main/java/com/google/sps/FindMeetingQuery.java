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
    // create a full day of free timeslots
    List<TimeRange> fullDay = new ArrayList<TimeRange>();
    fullDay.add(TimeRange.WHOLE_DAY);
    
    // cannot modify arraylist in an enhanced for loop, must create separate arraylists
    List<TimeRange> freeTimes = new ArrayList<TimeRange>();
    List<TimeRange> blockedTimes = new ArrayList<TimeRange>();


    Collection<String> attendees = request.getAttendees();
    Set<String> eventAttendees;
    
  for(Event event: events){
    eventAttendees = event.getAttendees();
    boolean isOverlappedAttendee = false;
    for(String attendee:eventAttendees){
      if(attendees.contains(attendee)){
        isOverlappedAttendee = true;
        // add a break to terminate loop early if at least one attendee is present at event
        break; 
        }
      }
      if(isOverlappedAttendee){
        TimeRange blocked = event.getWhen();
        for(TimeRange timeslot: fullDay){
          if(blocked.overlaps(timeslot)){
            // case one: event is fully enclosed within a free timeslot
            if(blocked.start() > timeslot.start() && blocked.end() < timeslot.end()){
              freeTimes.add(TimeRange.fromStartEnd(timeslot.start(), blocked.start(),false));
              freeTimes.add(TimeRange.fromStartEnd(blocked.end(),timeslot.end(),false));
            }
            // case two: event overlaps end of free timeslot
            else if(blocked.start() >= timeslot.start() && blocked.end() >= timeslot.end()){
              if(blocked.start()  != timeslot.start()){
                freeTimes.add(TimeRange.fromStartEnd(timeslot.start(), blocked.start(),false));
              }
            }
            // case three: event overlaps start of free timeslot
            else if(blocked.start() <= timeslot.start() && blocked.end() >= timeslot.start()){
              if(blocked.end()  != timeslot.end()){
                freeTimes.add(TimeRange.fromStartEnd(blocked.end(),timeslot.end(),false));
              }
            }
            blockedTimes.add(timeslot);
          }
        }
        fullDay.addAll(freeTimes);
        fullDay.removeAll(blockedTimes);
        // must clear everytime so that blocked events don't get added back
        freeTimes.clear();
        blockedTimes.clear();
      }
    }
        
    // filter through available times to find sufficient durations
    for(TimeRange available: fullDay){
      if(available.duration() < request.getDuration()){
          blockedTimes.add(available);
      }
    }
    fullDay.removeAll(blockedTimes);
    
    // try to insert optional attendees into existing timeslots 
    attendees = request.getOptionalAttendees();
    for(Event event: events){
        eventAttendees = event.getAttendees();
        boolean isOverlappedOptAttendee = false;
        for(String attendee:eventAttendees){
            if(attendees.contains(attendee)){
              isOverlappedOptAttendee = true;
              break; 
            }
        }
        if(isOverlappedOptAttendee){
          TimeRange blocked = event.getWhen();
              int start = blocked.start();
              int end = blocked.end();
              for(TimeRange timeslot: fullDay){
                if(blocked.overlaps(timeslot) && timeslot.duration()-blocked.duration() >= request.getDuration()){
                    if(blocked.start() > timeslot.start() && blocked.end() < timeslot.end()){
                        freeTimes.add(TimeRange.fromStartEnd(timeslot.start(), blocked.start(),false));
                        freeTimes.add(TimeRange.fromStartEnd(end,timeslot.end(),false));
                        // added to all cases bc there is a case where the free timeslot should be kept
                        blockedTimes.add(timeslot); 
                    }
                    else if(blocked.start() >= timeslot.start() && blocked.end() <= timeslot.end()){
                      if(blocked.start() != timeslot.start()){
                      freeTimes.add(TimeRange.fromStartEnd(timeslot.start(), blocked.start(),false));
                      }
                      blockedTimes.add(timeslot);
                    }
                    else if(blocked.start()<= timeslot.start() && blocked.end() <= timeslot.start()){
                        if(blocked.end() != timeslot.end()){
                            freeTimes.add(TimeRange.fromStartEnd(blocked.end(),timeslot.end(),false));
                        }
                        blockedTimes.add(timeslot);
                    }
                }
              }
              fullDay.addAll(freeTimes);
              fullDay.removeAll(blockedTimes);
              freeTimes.clear();
              blockedTimes.clear();

        }
    }
    return fullDay;
  }
}
 
 
 
 
 

