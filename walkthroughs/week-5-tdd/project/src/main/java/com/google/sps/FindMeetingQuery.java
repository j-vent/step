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

  /* filter through timeslots to find those with sufficient durations */
  private void filterForSufficientDuration(List<TimeRange> fullDay, List<TimeRange> freeTimes, List<TimeRange> blockedTimes, long requestDuration){
    for(TimeRange available: fullDay){
      if(available.duration() < requestDuration){
        blockedTimes.add(available);
      }
    }
    fullDay.removeAll(blockedTimes);
  }

  /* update fullDay to reflect recently added attendees' events */
  private void updateAvailability(List<TimeRange> fullDay, List<TimeRange> freeTimes, List<TimeRange> blockedTimes){
    fullDay.addAll(freeTimes);
    fullDay.removeAll(blockedTimes);
    // cleared so previously blocked times don't get added back 
    freeTimes.clear();
    blockedTimes.clear();
  }

  private void addOverlappedMandatoryAttendee(List<TimeRange> fullDay, List<TimeRange> freeTimes, List<TimeRange> blockedTimes, TimeRange blocked){
    for(TimeRange timeslot: fullDay){
      if(blocked.overlaps(timeslot)){
        // case one: event is fully enclosed within a free timeslot
        if(blocked.start() >= timeslot.start() && blocked.end() <= timeslot.end()){
          freeTimes.add(TimeRange.fromStartEnd(timeslot.start(), blocked.start(),false));
          freeTimes.add(TimeRange.fromStartEnd(blocked.end(),timeslot.end(),false));
        }
        // case two: event overlaps end of free timeslot
        else if(blocked.start() > timeslot.start() && blocked.end() > timeslot.end()){
          if(blocked.start()  != timeslot.start()){
            freeTimes.add(TimeRange.fromStartEnd(timeslot.start(), blocked.start(),false));
          }
        }
        // case three: event overlaps start of free timeslot
        else if(blocked.start() < timeslot.start() && blocked.end() > timeslot.start()){
          if(blocked.end()  != timeslot.end()){
            freeTimes.add(TimeRange.fromStartEnd(blocked.end(),timeslot.end(),false));
          }
        }
        blockedTimes.add(timeslot);
        }
      }
      updateAvailability(fullDay, freeTimes, blockedTimes);
  }

  private void addOverlappedOptAttendee(List<TimeRange> fullDay, List<TimeRange> freeTimes, List<TimeRange> blockedTimes, TimeRange blocked, long requestDuration){
      for(TimeRange timeslot: fullDay){
        // optional attendee's timeslot must retain the required duration of a meeting
        if(blocked.overlaps(timeslot) && timeslot.duration() - blocked.duration() >= requestDuration){
          if(blocked.start() >= timeslot.start() && blocked.end() <= timeslot.end()){
              if(blocked.start() != timeslot.start()){
                freeTimes.add(TimeRange.fromStartEnd(timeslot.start(), blocked.start(),false));
                freeTimes.add(TimeRange.fromStartEnd(blocked.end(),timeslot.end(),false));
              }
                blockedTimes.add(timeslot); 
          }
        }
      }
      updateAvailability(fullDay, freeTimes, blockedTimes);
  }

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    List<TimeRange> fullDay = new ArrayList<TimeRange>();
  
    // cannot modify arraylist in an enhanced for loop, must create separate arraylists
    List<TimeRange> freeTimes = new ArrayList<TimeRange>();
    List<TimeRange> blockedTimes = new ArrayList<TimeRange>();

    // create a full day of free timeslots
    fullDay.add(TimeRange.WHOLE_DAY);

    Collection<String> attendees = request.getAttendees();
    Set<String> eventAttendees;

    // insert mandatory attendees into existing timeslots
    for(Event event: events){
      for(String attendee:event.getAttendees()){
        if(!Collections.disjoint(attendees, event.getAttendees())){
          addOverlappedMandatoryAttendee(fullDay, freeTimes, blockedTimes, event.getWhen());
          // add a break to terminate loop early if at least one attendee is present at event
        }
      }
    }
    
    filterForSufficientDuration(fullDay, freeTimes, blockedTimes, request.getDuration());
    
    // insert optional attendees into existing timeslots 
    attendees = request.getOptionalAttendees();
    for(Event event: events){
        for(String attendee:event.getAttendees()){
            if(!Collections.disjoint(attendees, event.getAttendees())){
              addOverlappedOptAttendee(fullDay, freeTimes, blockedTimes, event.getWhen(), request.getDuration());
            }
        }
    }
    return fullDay;
  }
}
 
 
 
 
 

