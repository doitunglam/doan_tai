import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import authentication from './authentication';
import applicationProfile from './application-profile';

import administration from 'app/modules/administration/administration.reducer';
import userManagement from 'app/modules/administration/user-management/user-management.reducer';
import register from 'app/modules/account/register/register.reducer';
import activate from 'app/modules/account/activate/activate.reducer';
import password from 'app/modules/account/password/password.reducer';
import settings from 'app/modules/account/settings/settings.reducer';
import passwordReset from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import restaurant from 'app/entities/restaurant/restaurant.reducer';
// prettier-ignore
import services from 'app/entities/services/services.reducer';
// prettier-ignore
import imageServices from 'app/entities/image-services/image-services.reducer';
// prettier-ignore
import category from 'app/entities/category/category.reducer';
// prettier-ignore
import pay from 'app/entities/pay/pay.reducer';
// prettier-ignore
import invoice from 'app/entities/invoice/invoice.reducer';
// prettier-ignore
import detailInvoice from 'app/entities/detail-invoice/detail-invoice.reducer';
// prettier-ignore
import comment from 'app/entities/comment/comment.reducer';
// prettier-ignore
import hotel from 'app/entities/hotel/hotel.reducer';
// prettier-ignore
import roomHotel from 'app/entities/room-hotel/room-hotel.reducer';
// prettier-ignore
import typeRoom from 'app/entities/type-room/type-room.reducer';
// prettier-ignore
import priceRoom from 'app/entities/price-room/price-room.reducer';
// prettier-ignore
import detailInvoiceRoom from 'app/entities/detail-invoice-room/detail-invoice-room.reducer';
// prettier-ignore
import imageRoom from 'app/entities/image-room/image-room.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const rootReducer = {
  authentication,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  restaurant,
  services,
  imageServices,
  category,
  pay,
  invoice,
  detailInvoice,
  comment,
  hotel,
  roomHotel,
  typeRoom,
  priceRoom,
  detailInvoiceRoom,
  imageRoom,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
};

export default rootReducer;
