export interface ICheckList {
  id: number;
  name?: string | null;
}

export type NewCheckList = Omit<ICheckList, 'id'> & { id: null };
