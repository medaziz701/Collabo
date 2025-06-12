import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CrudService } from '../service/crud.service';
import { ActivatedRoute } from '@angular/router';
import { Tache } from '../Entity/tache.Entity';


@Component({
  selector: 'app-code-editor',
  templateUrl: './code-editor.component.html',
  styleUrls: ['./code-editor.component.css']
})
export class CodeEditorComponent implements OnInit {
  // Variables d'état
  currentEditId: number | null = null;
  visibleFiles: Set<number> = new Set();//on inisialise set vide pour  gérer l'état de visibilité des morceaux de code Pas de doublons chaque code est unique dans visibleFiles.
  visibleProjectFiles: Set<number> = new Set();
  visibleAuthors: Set<string> = new Set();
  visibleProjectAuthors: Set<string> = new Set();

  
  
  // Données
  selectedFile: File | null = null;
archives: any[] = [];
showArchives = false;
  allProjectCodeParts: any[] = [];
  showAllProjectCodes: boolean = false;
  selectedPart: any = null;
  editedContent: string = '';
  tache: any;
  codeParts: any[] = [];
  
  // Formulaire
  newPart = {
    filename: '',
    content: '',
    author: ''
  };
  selectedCodePartId: number | null = null;
  // États
  loading = true;
  error: string | null = null;
  currentUser: any;
  isCurrentUserAssigned: boolean = false;
projet: any;

  constructor(
    private crudService: CrudService,
    private route: ActivatedRoute,
    private cdRef: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    
    this.currentUser = this.crudService.getDeveloppeurInfo();
    
    this.route.paramMap.subscribe(params => {
      const tacheId = params.get('id');
      if (tacheId) {
        this.loadTache(+tacheId);
      } else {
        this.error = "ID de tâche non spécifié";
        this.loading = false;
      }
    });
  }
  
selectCodePart(id: number): void {
  this.selectedCodePartId = id;
}
  // Méthodes d'édition
  editCodePart(part: any) {
    this.currentEditId = part.id;//permet d’identifier la partie en cours d’édition.
    this.selectedPart = {...part};// Copie pour ne pas modifier l'original
    this.editedContent = part.content;
    this.visibleFiles.add(part.id);
    this.cdRef.detectChanges();//// Forcer Angular à détecter les changements
  }

  isEditing(part: any): boolean {
    return this.currentEditId === part.id && this.isAuthor(part);
  }

  cancelEdit() {
    this.currentEditId = null;
    this.selectedPart = null;
    this.editedContent = '';
  }

  saveEditedPart() {
    if (this.selectedPart) {
      const updatedPart = {
        ...this.selectedPart,
        content: this.editedContent
      };
      
      this.crudService.updateCodePart(this.selectedPart.id, updatedPart).subscribe({
        next: () => {
          this.cancelEdit();
          this.loadCodeParts(this.tache.id);
        },
        error: (err) => {
          console.error('Erreur:', err);
          this.error = 'Échec de la mise à jour du code';
        }
      });
    }
  }

  // Méthodes d'autorisation
  isAuthor(part: any): boolean {
    if (!this.currentUser || !part.author) return false;
    return part.author === `${this.currentUser.nom} ${this.currentUser.prenom}`;
  }

  canEditCodePart(part: any): boolean {
    return this.isCurrentUserAssigned && this.isAuthor(part);
  }

  // Méthodes de chargement
  loadTache(tacheId: number): void {
    this.loading = true;
    this.crudService.getTacheById(tacheId).subscribe({
      next: (tache) => {
        this.tache = tache;
        this.isCurrentUserAssigned = this.checkIfUserIsAssigned(tache);
        this.loadCodeParts(tacheId);
        this.loadAllProjectCodes(tache.projet.id);
        this.loading = false;
      },
      error: (err) => {
        this.error = "Erreur lors du chargement de la tâche";
        this.loading = false;
        console.error(err);
      }
    });
  }

  loadCodeParts(tacheId: number): void {
    this.crudService.getCodePartsByTache(tacheId).subscribe({
      next: (parts) => {
        this.codeParts = parts;
        this.newPart.author = `${this.currentUser.nom} ${this.currentUser.prenom}`;
      },
      error: (err) => {
        console.error("Erreur lors du chargement des parties de code:", err);
      }
    });
  }

  loadAllProjectCodes(projetId: number): void {
    this.crudService.getCodePartsByProjet(projetId).subscribe({
      next: (parts) => {
        this.allProjectCodeParts = parts;
      },
      error: (err) => {
        console.error("Erreur lors du chargement des codes du projet:", err);
      }
    });
  }

  
  checkIfUserIsAssigned(tache: Tache): boolean {
    return this.currentUser && 
           (tache.assigneA?.id === this.currentUser.id || 
            tache.projet?.equipe?.membres?.some(m => m.id === this.currentUser.id));
  }

  addCodePart(): void {
    if (!this.tache || !this.newPart.filename || !this.newPart.content) {
      this.error = "Veuillez remplir tous les champs";
      return;
    }
  
    const codePart = {
      filename: this.newPart.filename,
      content: this.newPart.content,
      author: this.newPart.author,
      tache: { id: this.tache.id },
      projet: { id: this.tache.projet.id }
    };
  
    this.crudService.addCodePart(codePart).subscribe({
      next: () => {
        this.loadCodeParts(this.tache.id);
        this.newPart = { 
          filename: '', 
          content: '', 
          author: this.newPart.author 
        };
        this.error = null;
      },
      error: (err) => {
        console.error("Erreur:", err);
        this.error = "Erreur lors de l'ajout du code";
      }
    });
  }

  deleteCodePart(part: any) {
    if (confirm('Voulez-vous vraiment supprimer ce code ?')) {
      this.crudService.deleteCodePart(part.id).subscribe({
        next: () => {
          this.loadCodeParts(this.tache.id);
        },
        error: (err) => {
          console.error('Erreur:', err);
          this.error = 'Échec de la suppression du code';
        }
      });
    }
  }

  // Méthodes de visibilité
  toggleFileVisibility(fileId: number): void {
    if (this.visibleFiles.has(fileId)) {
      this.visibleFiles.delete(fileId);
    } else {
      this.visibleFiles.add(fileId);
    }
  }

  isFileVisible(fileId: number): boolean {
    return this.visibleFiles.has(fileId);
  }
marquerTacheTerminee() {
  if (confirm('Voulez-vous vraiment marquer cette tâche comme terminée ?')) {
    this.crudService.marquerTacheTerminee(this.tache.id).subscribe({
      next: (tache) => {
        this.tache = tache;
        alert('Tâche marquée comme terminée avec succès!');
        
        // Vérifier si toutes les tâches sont terminées
        this.crudService.toutesTachesTerminees(this.tache.projet.id).subscribe({
          next: (toutesTerminees) => {
            if (toutesTerminees) {
              alert('Toutes les tâches de ce projet sont terminées !');
            }
          }
        });
      },
      error: (err) => {
        console.error('Erreur:', err);
        this.error = 'Échec de la mise à jour du statut';
      }
    });
  }
}

  // Groupement par auteur
getAuthorsGrouped(): {author: string, parts: any[]}[] {
  const authorsMap = new Map<string, any[]>();
  
  this.codeParts.forEach(part => {
    if (!authorsMap.has(part.author)) {
      authorsMap.set(part.author, []);
    }
    authorsMap.get(part.author)?.push(part);
  });
  
  return Array.from(authorsMap.entries()).map(([author, parts]) => ({
    author,
    parts
  }));
}
//Regroupe les parties de code de la tâche par auteur
getProjectAuthorsGrouped(): {author: string, parts: any[]}[] {
  const authorsMap = new Map<string, any[]>();
  
  this.allProjectCodeParts.forEach(part => {
    if (!authorsMap.has(part.author)) {
      authorsMap.set(part.author, []);
    }
    authorsMap.get(part.author)?.push(part);
  });
  
  return Array.from(authorsMap.entries()).map(([author, parts]) => ({
    author,
    parts
  }));
}
//Visibilité par auteur
toggleAuthorVisibility(author: string): void {
  if (this.visibleAuthors.has(author)) {
    this.visibleAuthors.delete(author);
  } else {
    this.visibleAuthors.add(author);
  }
}

isAuthorVisible(author: string): boolean {
  return this.visibleAuthors.has(author);
}
//Gère l’affichage ou le masquage du code d’un auteur donné
toggleProjectAuthorVisibility(author: string): void {
  if (this.visibleProjectAuthors.has(author)) {
    this.visibleProjectAuthors.delete(author);
  } else {
    this.visibleProjectAuthors.add(author);
  }
}

isProjectAuthorVisible(author: string): boolean {
  return this.visibleProjectAuthors.has(author);
}



//Visibilité fichier projet

toggleProjectFileVisibility(fileId: number): void {
  if (this.visibleProjectFiles.has(fileId)) {
    this.visibleProjectFiles.delete(fileId);
  } else {
    this.visibleProjectFiles.add(fileId);
  }
}

isProjectFileVisible(fileId: number): boolean {
  return this.visibleProjectFiles.has(fileId);
}

}