import { Component, Input, OnInit } from '@angular/core';
import { CrudService } from '../service/crud.service';
@Component({
  selector: 'app-comment-section',
  templateUrl: './comment-section.component.html',
  styleUrls: ['./comment-section.component.css']
})
export class CommentSectionComponent implements OnInit {
  @Input() codePartId!: number;//!: indique que la valeur sera bien définie
  @Input() projetId!: number;//Input permet de recevoir des valeurs depuis le composant parent.
  newComment = '';
  comments: any[] = [];
  chefId!: number;

  constructor(private crud: CrudService) {}

  ngOnInit() {
    this.crud.getChefIdByProjet(this.projetId).subscribe(chefId => {
      this.chefId = chefId;
      this.loadComments();
    });
  }

  addComment() {
    const user = this.crud.getDeveloppeurInfo();
    this.crud.createComment(
      this.codePartId,
      this.newComment,
      `${user.nom} ${user.prenom}`
    ).subscribe(() => {
      this.newComment = '';
      this.loadComments();
    });
  }

  private loadComments() {
    this.crud.getCommentsByCodePart(this.codePartId).subscribe(res => {
      this.comments = res;
    });
  }
}