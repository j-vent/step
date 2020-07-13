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
    List<Integer> fullDay = new ArrayList<Integer>();
    
    for(int i=0; i < 2531;i=i+30){
        fullDay.add(i);
    }
    System.out.println(fullDay);
    
    // throw new UnsupportedOperationException("TODO: Implement this method.");
    
    Collection<String> attendees = request.getAttendees();
    // this is some horrific n^3 runtime
    for(Event event: events){
        Set<String> eventAttendees = event.getAttendees();
        for(String attendee:eventAttendees){
            if(attendees.contains(attendee)){
                TimeRange blocked = event.getWhen();
                System.out.println(blocked);
                // mark out booked timeslots with -1
                int start = blocked.start()/30;
                int end = blocked.end()/30;
                for(int j = start; j < end; j++){
                    fullDay.set(j,-1);
                }
                
            }
        }
    }

    System.out.println(fullDay);
    return new ArrayList<TimeRange>();
    // get all the remaining, free time slots
    // filter time slots by required duration of meeting
  }
}
