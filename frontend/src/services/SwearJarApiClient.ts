import axios from 'axios';

export interface ApiResponse {
    errorMessage: string;
}

export interface MemberInfo {
    id: number;
    name: string;
    amountCalculated: number;
    amountAdjusted: number;
}

export interface DashboardResponse extends ApiResponse {
    members: MemberInfo[];
}

interface AddPenaltyRequest {
    memberName: string;
    amount: number;
}

interface ChangePenaltyRequest {
    id: number;
    amount: number;
}

interface ChangePenaltyResponse {
    id: number;
    amount: number;
}

export interface AddPenaltyResponse extends ApiResponse {
    memberId: number;
    memberName: string;
    amount: number;
    calculatedTotal: number;
}

interface AdjustPenaltyTotalRequest {
    memberId: number;
    amount: number;
}

export interface AdjustPenaltyTotalResponse extends ApiResponse {
    memberId: number;
    amount: number;
}

export interface PenaltyInfo {
    id: number;
    dateTime: string;
    amount: number;
}

export interface TeamMemberDetailResponse extends ApiResponse {
    id: number;
    name: string;
    amountCalculated: number;
    amountAdjusted: number;
    penalties: PenaltyInfo[];
}

export class SwearJarApiClient {

    private getBaseUrl(): string {
        return 'http://localhost:8080/api/';
    }

    private getUrlForPath(path: string): string {
        return this.getBaseUrl() + path;
    }

    async dashboard(): Promise<DashboardResponse> {
        const response = await axios.get<DashboardResponse>(
            this.getUrlForPath('dashboard'),
            {
                headers: {
                    Accept: 'application/json'
                }
            }
        );
        return Promise.resolve(response.data);
    }

    async addPenalty(memberName: string, amount: number): Promise<AddPenaltyResponse> {
        const request: AddPenaltyRequest = { memberName, amount };
        const response = await axios.post<AddPenaltyRequest, AddPenaltyResponse>(
            this.getUrlForPath('penalty'),
            request,
            {
                headers: {
                    Accept: 'application/json'
                }
            }
        );
        return Promise.resolve(response.data);
    }

    async changePenalty(id: number, amount: number): Promise<ChangePenaltyResponse> {
        const request: ChangePenaltyRequest = { id, amount };
        const response = await axios.patch<ChangePenaltyRequest, ChangePenaltyResponse>(
            this.getUrlForPath('penalty'),
            request,
            {
                headers: {
                    Accept: 'application/json'
                }
            }
        );
        return Promise.resolve(response.data);
    }

    async removePenalty(id: number): Promise<ApiResponse> {
        const response = await axios.delete(
            this.getUrlForPath('penalty/' + id),
            {
                headers: {
                    Accept: 'application/json'
                }
            }
        );
        return Promise.resolve(response.data);
    }

    async adjustPenaltyTotal(memberId: number, amount: number): Promise<AdjustPenaltyTotalResponse> {
        const request: AdjustPenaltyTotalRequest = { memberId, amount };
        const response = await axios.post<AdjustPenaltyTotalRequest, AdjustPenaltyTotalResponse>(
            this.getUrlForPath('penalty_total'),
            request,
            {
                headers: {
                    Accept: 'application/json'
                }
            }
        );
        return Promise.resolve(response.data);
    }

    async teamMemberDetail(memberId: number): Promise<TeamMemberDetailResponse> {
        const response = await axios.get<TeamMemberDetailResponse>(
            this.getUrlForPath('team_member/' + memberId),
            {
                headers: {
                    Accept: 'application/json'
                }
            }
        );
        return Promise.resolve(response.data);
    }
}
