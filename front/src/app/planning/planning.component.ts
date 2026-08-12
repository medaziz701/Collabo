import { Component } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { CrudService } from '../service/crud.service';
import { Router } from '@angular/router';
import { isSameDay, isSameMonth } from 'date-fns';//Fonctions utilitaires pour comparer des dates  savoir si deux dates sont le même jour ou le même mois.
import { Subject } from 'rxjs';//RxJS pour déclencher manuellement une mise à jour du calendrier
import Swal from 'sweetalert2';//Importe SweetAlert2 pour afficher des popups stylisés comme succès, erreur
import { SavePlanningC } from '../Entity/SavePlanningC.Entity';


// Types du calendrier
import {
  CalendarEvent,
  CalendarEventTimesChangedEvent,
  CalendarView,
  CalendarMonthViewDay
} from 'angular-calendar';//Importe les types du module angular-calendar nécessaires pour afficher et interagir avec le calendrier

@Component({
  selector: 'app-planning',
  templateUrl: './planning.component.html',
  styleUrls: ['./planning.component.css']
})
export class PlanningComponent {


  PlanningCForm: FormGroup;

  constructor(
    private service: CrudService,
    private router: Router,
    private fb: FormBuilder 
  ) {
    let formControls = {//Création des contrôles du formulaire avec validation required
      nom: new FormControl('', [Validators.required]),
      date: new FormControl('', [Validators.required]),
      debut: new FormControl('', [Validators.required]),
      fin: new FormControl('', [Validators.required]),
    };
    this.PlanningCForm = this.fb.group(formControls);
  }
  get nom() {
    return this.PlanningCForm.get('nom');
  }
  get date() {
    return this.PlanningCForm.get('date');
  }
  get debut() {
    return this.PlanningCForm.get('debut');
  }
  get fin() {
    return this.PlanningCForm.get('fin');
  }
  ///Gère le clic sur un jour du calendrier Si on clique sur le même jour deux fois il ferme l'affichage Sinon il l’ouvre
  handleDayClick({ date, events }: { date: Date; events: CalendarEvent[] }): void {
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
  addNewPlanningC() {
    // First check if form is valid
    if (this.PlanningCForm.invalid) {
      Swal.fire({
        title: 'Erreur',
        text: 'Veuillez remplir tous les champs correctement.',
        icon: 'error',
        showConfirmButton: false,
        timer: 3000,
      });
      return;
    }
  
    // Get user info with proper null checks
   
    const chefEquipeInfo = this.service.getChefEquipeInfo();
    
    // Validate chefEquipe ID exists
    if (!chefEquipeInfo?.id) {
      Swal.fire({
        title: 'Erreur',
        text: 'Impossible de récupérer les informations du chef d\'équipe. Veuillez vous reconnecter.',
        icon: 'error',
        showConfirmButton: false,
        timer: 3000,
      });
      return;
    }
  
    const formData = this.PlanningCForm.value;
  
    // Create the planning data object
    const planningData: SavePlanningC = {
      nom: formData.nom,
      date: formData.date,
      debut: formData.debut,
      fin: formData.fin,
      idChefEquipe: chefEquipeInfo.id  // Now guaranteed to have a value
    };
  
    // Add the planning with better error handling
    this.service.addPlanningC(planningData).subscribe({
      next: (res) => {
        Swal.fire({
          title: 'Succès !',
          text: 'Planning ajouté avec succès.',
          icon: 'success',
          showConfirmButton: false,
          timer: 3000,
        });
        this.viewDate = new Date(formData.date);
        this.fetchAndDisplayPlanningData();
        this.PlanningCForm.reset();
      },
      error: (err) => {
        console.error('Erreur lors de l\'ajout du planning:', err);
        let errorMessage = 'Une erreur est survenue lors de l\'ajout du planning.';
        
        if (err.status === 500 && err.error?.message?.includes('must not be null')) {
          errorMessage = 'Erreur: L\'ID du chef d\'équipe est manquant.';
        } else if (err.error?.message) {
          errorMessage = err.error.message;
        }
        
        Swal.fire({
          title: 'Erreur',
          text: errorMessage,
          icon: 'error',
          showConfirmButton: false,
          timer: 3000,
        });
      }
    });
  }
  view: CalendarView = CalendarView.Month;//view: vue actuelle (mois, semaine, jour)
  CalendarView = CalendarView;
  viewDate: Date = new Date();//viewDate: date sélectionnée
  events: CalendarEvent[] = [//events: événements à afficher
    
  ];
  refresh: Subject<any> = new Subject();
  activeDayIsOpen: boolean = true;

  setView(view: CalendarView) {//Change la vue du calendrier.
    this.view = view;
  }

  closeOpenMonthViewDay() {//Ferme le jour sélectionné.
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
  }

  eventTimesChanged({
    event,
    newStart,
    newEnd,
  }: CalendarEventTimesChangedEvent): void {
  }
// Chargement des plannings depuis l'API
  addEvent(): void {}
  fetchAndDisplayPlanningData() {
    let chefInfo = this.service.getChefEquipeInfo();
    if (!chefInfo?.id) {
        console.error('Aucun ID chef équipe trouvé');
        return;
    }

    this.service.getPlanningC(chefInfo.id).subscribe(
        (planningData: any[]) => {
            console.log('Données reçues:', planningData);
            this.events = [];
            const today = new Date();

            planningData.forEach((data) => {
                try {
                    const dateStr = data.date.split('T')[0];
                    const startDateTime = `${dateStr}T${data.debut}`;
                    const endDateTime = `${dateStr}T${data.fin}`;

                    const eventStart = new Date(startDateTime);
                    const eventEnd = new Date(endDateTime);

                    const color = eventEnd < today 
                        ? 'rgba(255, 0, 0, 0.5)'
                        : 'rgba(30, 144, 255, 0.5)';

                    // Ajoutez les heures dans le titre
                    const eventTitle = `${data.nom} (${data.debut} - ${data.fin})`;

                    const event: CalendarEvent = {
                        start: eventStart,
                        end: eventEnd,
                        title: eventTitle, // Utilisez le nouveau titre avec les heures
                        color: {
                            primary: color,
                            secondary: color,
                        },
                        allDay: false
                    };

                    this.events.push(event);
                } catch (e) {
                    console.error('Erreur de création d\'événement:', e, data);
                }
            });

            // Force le rafraîchissement du calendrier
            
            console.log('Événements créés:', this.events);
        },
        (err) => {
            console.error('Erreur de récupération des plannings:', err);
        }
    );
}
//Méthode appelée au démarrage
  ngOnInit(): void {
    this.fetchAndDisplayPlanningData();
  }


}

