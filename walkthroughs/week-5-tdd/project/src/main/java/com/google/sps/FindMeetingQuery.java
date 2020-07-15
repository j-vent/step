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
 
  /**
                |----------|
                    |----|
 
                     |--|
                    |-------|
 
                    |----|
                        |-----|
 
                    |-----|
                |----|
                **/
public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    // throw new UnsupportedOperationException("TODO: Implement this method.");

    List<TimeRange> fullDay = new ArrayList<TimeRange>();
    fullDay.add(TimeRange.WHOLE_DAY);
    
    Collection<String> attendees = request.getAttendees();
 
    // cannot modify arraylist in an enhanced for loop, must create separate arraylists
    List<TimeRange> freeTimes = new ArrayList<TimeRange>();
    List<TimeRange> blockedTimes = new ArrayList<TimeRange>();
    TimeRange blocked;
    int start, end;

    for(Event event: events){
        Set<String> eventAttendees = event.getAttendees();
        for(String attendee:eventAttendees){
            if(attendees.contains(attendee)){
              blocked = event.getWhen();
              start = blocked.start();
              end = blocked.end();
               
              for(TimeRange timeslot: fullDay){
                if(blocked.overlaps(timeslot)){
                    if(start > timeslot.start() && end < timeslot.end()){
                        freeTimes.add(TimeRange.fromStartEnd(timeslot.start(), start,false));
                        freeTimes.add(TimeRange.fromStartEnd(end,timeslot.end(),false));
                    }
                    else if(start >= timeslot.start() && end >= timeslot.end()){
                      if(start != timeslot.start()){
                      freeTimes.add(TimeRange.fromStartEnd(timeslot.start(), start,false));
                      }
                    }
                    else if(start <= timeslot.start() && end >= timeslot.start()){
                        if(end != timeslot.end()){
                            freeTimes.add(TimeRange.fromStartEnd(end,timeslot.end(),false));
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
    }

    for(TimeRange available: fullDay){
      if(available.duration() < request.getDuration()){
          blockedTimes.add(available);
      }
    }

    fullDay.removeAll(blockedTimes);
    return fullDay;
  }
}
 
 
 
 
 

