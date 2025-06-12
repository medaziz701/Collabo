import { Component, OnInit } from '@angular/core';
import { CrudService } from '../service/crud.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-liste-projet-client',
  templateUrl: './liste-projet-client.component.html',
  styleUrls: ['./liste-projet-client.component.css']
})
export class ListeProjetClientComponent implements OnInit {
  projets: any[] = [];
  loading = true;
  errorMessage = '';
  feedbackForm: FormGroup;
  showFeedbackForm = false;
  selectedProjetId: number | null = null;

  constructor(
    private service: CrudService,
    private fb: FormBuilder
  ) {
    this.feedbackForm = this.fb.group({
      noteEtoiles: [0, [Validators.required, Validators.min(1), Validators.max(5)]],
      commentaire: ['', [Validators.required, Validators.maxLength(500)]]
    });
  }

  ngOnInit(): void {
    this.getClientProjets();
  }
  

  getClientProjets(): void {
    const userInfo = this.service.getClientInfo();
    this.service.getProjetsByClientId(userInfo.id).subscribe(
      (data: any) => {
        this.projets = data;
        this.loading = false;
      },
      error => {
        this.errorMessage = 'Erreur lors du chargement des projets';
        this.loading = false;
      }
    );
  }
  openFeedbackForm(projetId: number): void {
    console.log('Opening feedback form for project:', projetId);
    this.selectedProjetId = projetId;
    this.showFeedbackForm = true;
    this.feedbackForm.reset({ noteEtoiles: 0 });
  }

  cancelFeedback(): void {
    this.showFeedbackForm = false;
    this.selectedProjetId = null;
  }

  submitFeedback(): void {
    if (this.feedbackForm.invalid || !this.selectedProjetId) {
      return;
    }

    const clientInfo = this.service.getClientInfo();
    const feedbackData = {
      ...this.feedbackForm.value,
      projet: { id: this.selectedProjetId },
      client: { id: clientInfo.id }
    };

    this.service.addFeedback(feedbackData).subscribe({
      next: (res) => {
        this.showFeedbackForm = false;
        this.selectedProjetId = null;
        this.getClientProjets();
      },
      error: (err) => {
        this.errorMessage = 'Erreur lors de l\'envoi du feedback';
        console.error(err);
      }
    });
  }

  getStarsArray(rating: number): any[] {//Array(5)  crée un tableau de 5 éléments  .fill(0) remplit le tableau avec des zéros (valeurs initiales)
    return Array(5).fill(0).map((_, i) => i < rating);//i représente l’index (de 0 à 4) Si l’index est inférieur à rating, renvoie true (étoile pleine)Sinon, false (étoile vide).
  }
}
//_ : indique que la valeur de l’élément n’est pas utilisée , i c’est l’index (position)
//exemple getStarsArray(3)  // Résultat : [true, true, true, false, false]
//i = 0 → 0 < 3 → true

//i = 1 → 1 < 3 → true

//i = 2 → 2 < 3 → true

//i = 3 → 3 < 3 → false

//i = 4 → 4 < 3 → false

//[true, true, true, false, false]
