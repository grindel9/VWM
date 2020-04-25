import { Product } from '../model/product';

export const PRODUCTS: Product[] = [
  {
    id: 1,
    brand: 'Lenovo',
    madeIn: 'czechia',
    model: 'Vagram',
    price: 666,
    screenSize: 128,
    type: 'Netbook',
    status: 'Availible'
  },
  {
    id: 2,
    brand: 'Asus',
    madeIn: 'czechia',
    model: 'Novia',
    price: 123,
    screenSize: 0,
    type: 'Desktop',
    status: 'Availible'
  },
  {
    id: 3,
    brand: 'Dell',
    madeIn: 'slovakia',
    model: 'Futurus',
    price: 666,
    screenSize: 128,
    type: 'Laptop',
    status: 'Broken'
  }
];