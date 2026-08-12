import { Component, OnInit } from '@angular/core';
import { CrudService } from '../service/crud.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  notifications: any[] = [];
private intervalId: any;
showNotifications = false;
unreadCount = 0;
  constructor(private service: CrudService) {}

  
  ngOnInit() {
  this.loadNotifications();
  this.intervalId = setInterval(() => this.loadNotifications(), 30000);
}
hasUnreadNotifications(): boolean {
  return this.notifications.some(n => !n.estLue);
}

  loadNotifications() {
  this.service.getNotifications().subscribe(
    (data: any[]) => {
      this.notifications = data.sort((a, b) => 
        new Date(b.date).getTime() - new Date(a.date).getTime()
      );
      this.unreadCount = this.notifications.filter(n => !n.estLue).length;
    },
    error => console.error(error)
  );
}

  markAsRead(notificationId: number) {
    this.service.markAsRead(notificationId).subscribe(() => {
      this.notifications = this.notifications.map(n => 
        n.id === notificationId ? {...n, estLue: true} : n
      );
    });
  }
  clearAll() {
  this.service.markAllAsRead().subscribe(() => {
    this.notifications = this.notifications.map(n => ({ ...n, estLue: true }));
  });
}



 
}
