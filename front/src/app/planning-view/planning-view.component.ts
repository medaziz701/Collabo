import { Component, OnInit } from '@angular/core';
import { CrudService } from '../service/crud.service';
import { isSameDay, isSameMonth } from 'date-fns';
import { Subject } from 'rxjs';
import {
  CalendarEvent,
  CalendarView,
} from 'angular-calendar';

@Component({
  selector: 'app-planning-view',
  templateUrl: './planning-view.component.html',
  styleUrls: ['./planning-view.component.css']
})
export class PlanningViewComponent implements OnInit {
  view: CalendarView = CalendarView.Month;
  CalendarView = CalendarView;
  viewDate: Date = new Date();
  events: CalendarEvent[] = [];
  refresh: Subject<any> = new Subject();
  activeDayIsOpen: boolean = true;

  constructor(private service: CrudService) { }

  setView(view: CalendarView) {
    this.view = view;
  }

  closeOpenMonthViewDay() {
    this.activeDayIsOpen = false;
  }

  dayClicked({ date, events }: { date: Date; events: CalendarEvent[] }): void {
    if (isSameMonth(date, this.viewDate)) {
      if (
        (isSameDay(this.viewDate, date) && this.activeDayIsOpen === true) ||
        events.length === 0
      ) {
        this.activeDayIsOpen = false;
      } else {
        this.activeDayIsOpen = true;
      }
      this.viewDate = date;
    }
  }

  handleEvent(action: string, event: CalendarEvent): void {
    console.log('Événement cliqué:', event);
  }

  fetchAndDisplayPlanningData() {
    const devInfo = this.service.getDeveloppeurInfo();
    
    if (devInfo?.id) {
      this.service.getPlanningByDeveloppeurId(devInfo.id).subscribe({
        next: (planningData) => {
          this.processPlanningData(planningData);
        },
        error: (err) => {
          console.error('Erreur:', err);
        }
      });
    }
  }

 private processPlanningData(planningData: any[]) {
    this.events = [];
    const today = new Date();

    planningData.forEach((data) => {
      try {
        const dateStr = data.date.split('T')[0];
        const startTime = data.debut.split(':');
        const endTime = data.fin.split(':');

        const start = new Date(dateStr);
        start.setHours(parseInt(startTime[0]), parseInt(startTime[1]));

        const end = new Date(dateStr);
        end.setHours(parseInt(endTime[0]), parseInt(endTime[1]));

        const isPastEvent = end < today;

        this.events.push({
          start,
          end,
          title: `${data.nom} (${data.debut} - ${data.fin})`,
          color: {
            primary: isPastEvent ? '#FF0000' : '#1E90FF',
            secondary: isPastEvent ? '#FFCCCB' : '#D1E8FF'
          },
          allDay: false,
          meta: {
            chef: `${data.chefEquipe.nom} ${data.chefEquipe.prenom}`,
            description: data.nom
          }
        });
      } catch (e) {
        console.error('Erreur création événement:', e, data);
      }
    });
  }


  ngOnInit(): void {
    this.fetchAndDisplayPlanningData();
  }
}