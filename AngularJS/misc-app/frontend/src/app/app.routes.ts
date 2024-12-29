import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { InstitutionComponent } from './institution/institution.component';
import { DirectoryComponent } from './directory/directory.component';
import { AssessmentFormComponent } from './assessmentform/assessmentform.component'
import { AdminPanelComponent } from './adminpanel/adminpanel.component'
import { InstitutionInfoComponent } from './institutioninfo/institutioninfo.component';
import { MemberPanelComponent } from './memberpanel/memberpanel.component';

export const routes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'institution', component: InstitutionComponent },
    { path: 'directory', component: DirectoryComponent },
    { path: 'directoryWithID/:id', component: DirectoryComponent },
    { path: 'assessmentform', component: AssessmentFormComponent },
    { path: 'adminpanel/:id/:username/:password', component: AdminPanelComponent },
    { path: 'institutioninfo', component: InstitutionInfoComponent },
    { path: 'memberpanel', component: MemberPanelComponent },
    { path: 'memberpanelWithID/:id/:username/:password', component: MemberPanelComponent },
    // { path: 'getinstitution'}
    // Add other routes as needed
  ];
