import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MessageService } from 'primeng/api';
import { DialogModule } from 'primeng/dialog';
import { IUser, IUserPage } from 'src/app/model/model.interfaces';
import { AdminUserDetailUnroutedComponent } from '../admin-user-detail-unrouted/admin-user-detail-unrouted.component';
import { ConfirmationService } from 'primeng/api';
import { DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import {ButtonModule} from 'primeng/button';
@Component({
  selector: 'app-admin-user-plist-unrouted',
  templateUrl: './admin-user-plist-unrouted.component.html',
  styleUrls: ['./admin-user-plist-unrouted.component.css'],
  providers: [ConfirmationService]
})
export class AdminUserPlistUnroutedComponent implements OnInit {
  oPage: any = [];
  orderField: string = 'id';
  orderDirection: string = 'asc';
  oPaginatorState: any = { first: 0, rows: 10, page: 0, pageCount: 0 };
  status: HttpErrorResponse | null = null;
  userToDelete: IUser | undefined;
  displayDialog: boolean = false;
  selectedUserId: number | undefined;


  constructor(
    private oHttpClient: HttpClient,
    public dialogService: DialogService,
    public messageService: MessageService,
    private confirmationService: ConfirmationService
  ) {}

  ngOnInit() {
    this.getPage();
  }

  getPage(): void {
    this.oHttpClient
      .get<IUserPage>(
        'http://localhost:8085/user' +
          '?size=' +
          this.oPaginatorState.rows +
          '&page=' +
          this.oPaginatorState.page +
          '&sort=' +
          this.orderField +
          ',' +
          this.orderDirection
      )
      .subscribe({
        next: (data: IUserPage) => {
          this.oPage = data;
          this.oPaginatorState.pageCount = data.totalPages;
          console.log(this.oPaginatorState);
        },
        error: (error: HttpErrorResponse) => {
          this.oPage.error = error;
          this.status = error;
        }
      });
  }

  onPageChang(event: any) {
    this.oPaginatorState.rows = event.rows;
    this.oPaginatorState.page = event.page;
    this.getPage();
  }

  doOrder(fieldorder: string) {
    this.orderField = fieldorder;
    if (this.orderDirection == 'asc') {
      this.orderDirection = 'desc';
    } else {
      this.orderDirection = 'asc';
    }
    this.getPage();
  }

  ref: DynamicDialogRef | undefined;

  goToView(u: IUser) {
    this.ref = this.dialogService.open(AdminUserDetailUnroutedComponent, {
      data: {
        id: u.id
      },
      header: 'View of user',
      width: '70%',
      contentStyle: { overflow: 'auto' },
      baseZIndex: 10000,
      maximizable: false
    });
  }


 


  
  confirmDelete(u: IUser) {
    this.userToDelete = u;
    this.selectedUserId = u.id; // Guardar el ID del usuario seleccionado
    this.displayDialog = true;
  
    this.confirmationService.confirm({
   
      accept: () => {
        this.deleteUser();
      },
      reject: () => {
        // Lógica en caso de rechazo de eliminación
      }
    });
  }
  deleteUser() {
    if (this.selectedUserId) {
      this.oHttpClient.delete(`http://localhost:8085/user/${this.selectedUserId}`).subscribe(
        () => {
          console.log('User successfully deleted');
          this.getPage();
          this.displayDialog = false;
        },
        (error: HttpErrorResponse) => {
          console.error('Error deleting user', error);
        }
      );
    }
  

  }
}