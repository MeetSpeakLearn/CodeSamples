import { Institution } from './institution';

export class Global {
    public static username : string = '';
    public static memberNumber: Number = 0;
    public static password : string = '';
    public static fname : string = '';
    public static institution : Institution | null = null;
    public static f2q: any = {
        call_num: 'cn',
        credit_union_name: 'cun',
        address: 'a',
        address2: 'a2',
        ncua_charter_number: 'ncn',
        date: 'd',
        total_shares_and_desposits: 'tsad',
        excess_shares_and_desposits: 'esad',
        previous_excess_high: 'peh',
        increase_in_shares_and_desposits: 'iisad',
        ncua_5300_agress: 'n5a',
        ncua_5300_memo: 'n5m',
        risk_standard_compliance: 'rsc',
        risk_standard_memo: 'rsm',
        certify: 'c',
        submitter: 's',
        title_position: 'tp',
        email_address: 'ea',
        date_submitted: 'ds',
    };
    public static q2f: any = {
        cn: 'call_num',
        cun: 'credit_union_name',
        a: 'address',
        a2: 'address2',
        ncn: 'ncua_charter_number',
        d: 'date',
        tsad: 'total_shares_and_desposits',
        esad: 'excess_shares_and_desposits',
        peh: 'previous_excess_high',
        iisad: 'increase_in_shares_and_desposits',
        n5a: 'ncua_5300_agress',
        n5m: 'ncua_5300_memo',
        rsc: 'risk_standard_compliance',
        rsm: 'risk_standard_memo',
        c: 'certify',
        s: 'submitter',
        tp: 'title_position',
        ea: 'email_address',
        ds: 'date_submitted',
    };
}