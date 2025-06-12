import { Component } from '@angular/core';
import { ZegoUIKitPrebuilt } from '@zegocloud/zego-uikit-prebuilt';
import { CrudService } from '../service/crud.service';

@Component({
  selector: 'app-video-call',
  templateUrl: './video-call.component.html',
  styleUrls: ['./video-call.component.css']
})
export class VideoCallComponent {
  constructor(private service: CrudService) { }
  IsChefEquipeIn: boolean;
  IsDeveloppeurIn: boolean;


  ngOnInit(): void {
    this.IsChefEquipeIn = this.service.isChefEquipeIn();
    
    this.IsDeveloppeurIn = this.service.isDeveloppeurIn();
    this.initializeZegoUIKit();
    
  }
//Définit les constantes appID et serverSecret nécessaires pour l’authentification avec ZegoCloud
  initializeZegoUIKit(): void {
    const appID = 647089597; // Remplacez par  AppID ZegoCloud, doit être un nombre
    const serverSecret = 'c2e82ec9f33329ebc3701e9eb12bddc3'; // Remplacez par ServerSecret ZegoCloud
    let DeveloppeurInfo = this.service.getDeveloppeurInfo();
    let ChefEquipeInfo = this.service.getChefEquipeInfo();
   //Génère un ID utilisateur local unique (avec timestamp)
    const localUserID = 'local_user_' + new Date().getTime();
    let localUserName = '';

    if (this.IsDeveloppeurIn) {
      localUserName = DeveloppeurInfo.nom;
    } else if (this.IsChefEquipeIn) {
      localUserName = ChefEquipeInfo.nom;
    } else {
      localUserName = 'anonymous';
    }
    const roomID = 'room_test'; // Utilisez le même roomID pour tous les utilisateurs
//Génère un token d’authentification pour l’utilisateur local.
    const localKitToken = ZegoUIKitPrebuilt.generateKitTokenForTest(appID, serverSecret, roomID, localUserID, localUserName);
    

    // Utilisateur distant
    const remoteUserID = 'remote_user_' + new Date().getTime();
    const remoteUserName = 'remote_user';

    const remoteKitToken = ZegoUIKitPrebuilt.generateKitTokenForTest(appID, serverSecret, roomID, remoteUserID, remoteUserName);

    // Joindre la salle en tant qu'utilisateur local
    const zegoUIKitPrebuilt = ZegoUIKitPrebuilt.create(localKitToken);
    zegoUIKitPrebuilt.joinRoom({
      container: document.getElementById('zego-container') as HTMLElement,
      scenario: {
        mode: ZegoUIKitPrebuilt.VideoConference,
      },
     
      maxUsers:4,
    
      

    });

    this.addRemoteUser(remoteKitToken);
  }

  addRemoteUser(remoteKitToken: string): void {
    const zegoUIKitRemote = ZegoUIKitPrebuilt.create(remoteKitToken);
    zegoUIKitRemote.joinRoom({
      container: document.getElementById('zego-container-remote') as HTMLElement,
      scenario: {
        mode: ZegoUIKitPrebuilt.VideoConference,
      }
    });
  }
}
